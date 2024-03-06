package com.example.memento.controller

import org.example.model.TestModel

class TestController(val model: TestModel) {
    // we can cast `Any` later since each event has an associated type

}