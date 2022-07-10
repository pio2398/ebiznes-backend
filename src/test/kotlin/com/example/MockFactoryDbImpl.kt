package com.example

import com.example.services.DatabaseFactory

class MockFactoryDbImpl : DatabaseFactory {
    override fun init() {
        print("test")
    }
}
