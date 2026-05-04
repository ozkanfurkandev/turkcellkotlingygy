-- =============================================================
--  borrow_records tablosu, RLS policy'leri ve atomik RPC fonksiyonları
--  Supabase SQL Editor üzerinde tek seferde çalıştırılabilir.
-- =============================================================

-- 1) Tablo
create table if not exists public.borrow_records (
    id           uuid        primary key default gen_random_uuid(),
    student_id   uuid        not null references auth.users(id) on delete cascade,
    book_id      uuid        not null references public.books(id) on delete cascade,
    borrowed_at  timestamptz not null default now(),
    due_date     date        not null,
    returned_at  timestamptz
);

create index if not exists borrow_records_student_idx on public.borrow_records(student_id);
create index if not exists borrow_records_book_idx    on public.borrow_records(book_id);

-- Aynı kitabı, iade etmeden ikinci kez ödünç almayı veritabanı seviyesinde engelle
create unique index if not exists borrow_records_unique_active
    on public.borrow_records(student_id, book_id)
    where returned_at is null;

-- 2) Row Level Security
alter table public.borrow_records enable row level security;

drop policy if exists "students_select_own_records" on public.borrow_records;
create policy "students_select_own_records"
    on public.borrow_records
    for select
    to authenticated
    using ( auth.uid() = student_id );

drop policy if exists "students_insert_own_records" on public.borrow_records;
create policy "students_insert_own_records"
    on public.borrow_records
    for insert
    to authenticated
    with check ( auth.uid() = student_id );

drop policy if exists "students_update_own_records" on public.borrow_records;
create policy "students_update_own_records"
    on public.borrow_records
    for update
    to authenticated
    using ( auth.uid() = student_id )
    with check ( auth.uid() = student_id );

-- 3) Atomik ödünç alma fonksiyonu
--    available_copies'i kilitleyip azaltır ve borrow_records'a kayıt atar.
create or replace function public.borrow_book(p_book_id uuid, p_days int)
returns public.borrow_records
language plpgsql
security definer
set search_path = public
as $$
declare
    v_user      uuid := auth.uid();
    v_available int;
    v_record    public.borrow_records;
begin
    if v_user is null then
        raise exception 'Yetkilendirme yok' using errcode = '42501';
    end if;

    if p_days is null or p_days < 1 or p_days > 5 then
        raise exception 'Kiralama süresi 1-5 gün arasında olmalıdır' using errcode = '22023';
    end if;

    -- Stok satırını kilitle
    select available_copies into v_available
    from public.books
    where id = p_book_id
    for update;

    if not found then
        raise exception 'Kitap bulunamadı' using errcode = 'P0002';
    end if;

    if v_available <= 0 then
        raise exception 'Kitap stokta yok' using errcode = 'P0001';
    end if;

    if exists (
        select 1
        from public.borrow_records
        where student_id = v_user
          and book_id    = p_book_id
          and returned_at is null
    ) then
        raise exception 'Bu kitap zaten ödünç alınmış' using errcode = 'P0001';
    end if;

    update public.books
       set available_copies = available_copies - 1
     where id = p_book_id;

    insert into public.borrow_records (student_id, book_id, due_date)
    values (v_user, p_book_id, current_date + p_days)
    returning * into v_record;

    return v_record;
end;
$$;

grant execute on function public.borrow_book(uuid, int) to authenticated;

-- 4) (Opsiyonel) İade fonksiyonu - aktif kaydı kapatır, stok arttırır.
create or replace function public.return_book(p_record_id uuid)
returns public.borrow_records
language plpgsql
security definer
set search_path = public
as $$
declare
    v_user   uuid := auth.uid();
    v_record public.borrow_records;
begin
    if v_user is null then
        raise exception 'Yetkilendirme yok' using errcode = '42501';
    end if;

    select * into v_record
    from public.borrow_records
    where id = p_record_id
      and student_id = v_user
    for update;

    if not found then
        raise exception 'Kayıt bulunamadı' using errcode = 'P0002';
    end if;

    if v_record.returned_at is not null then
        raise exception 'Kitap zaten iade edilmiş' using errcode = 'P0001';
    end if;

    update public.borrow_records
       set returned_at = now()
     where id = p_record_id
    returning * into v_record;

    update public.books
       set available_copies = available_copies + 1
     where id = v_record.book_id;

    return v_record;
end;
$$;

grant execute on function public.return_book(uuid) to authenticated;
