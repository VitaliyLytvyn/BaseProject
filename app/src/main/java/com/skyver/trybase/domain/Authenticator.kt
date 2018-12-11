package com.skyver.trybase.domain

interface Authenticator{

    fun isLogedIn():Boolean

    fun logOutUser()
}