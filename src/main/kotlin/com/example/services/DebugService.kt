package com.example.services

interface DebugService {
    fun debug(): String
}

class DebugServiceImpl() : DebugService {
    override fun debug(): String {
        return "OK"
    }
}
