package com.ozkanfurkan.turkcellintro.di

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://lqdlewuvfadrqxpatgdg.supabase.co", //local.properties
        supabaseKey = "sb_publishable__56iluzuZmwiPpEdXxq8qw_znOrZNjo"
    ) {
        install(Postgrest)
    }
}