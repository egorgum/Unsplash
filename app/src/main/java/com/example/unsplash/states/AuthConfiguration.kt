package com.example.unsplash.states

import net.openid.appauth.ResponseTypeValues


object AuthConfiguration {
    const val AUTH_ENDPOINT = "https://unsplash.com/oauth/authorize"
    const val TOKEN_ENDPOINT = "https://unsplash.com/oauth/token"

    const val END_SESSION_URI = "https://unsplash.com/logout"
    const val REDIRECT_URI = "ru.arbonik.oauth://skillbox.ru/auth"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val ACCESS_KEY = "gw7XXy1dwNFwu6DN8hR5bw3zWJAVWPcFc6ldw8bWpPU"
    const val SECRET_KEY = "PwS_A9OVlYn8Fresp_KHK5qzgRLme8iPnwGeaDQH-vE"
    const val SCOPE = "public " +
            "read_user " +
            "write_user " +
            "read_photos " +
            "write_photos " +
            "write_likes " +
            "write_followers " +
            "read_collections " +
            "write_collections"
}