package ru.sliva.zapp.data.utils

import kotlin.reflect.full.instanceParameter

@Suppress("UNCHECKED_CAST")
fun <T: Any> T.copyOrNull() = javaClass.kotlin
    .takeIf { it.isData }
    ?.members?.firstOrNull { it.name == "copy" || it.name == "clone" }
    ?.let { callable ->
        callable.instanceParameter?.let {
            callable.callBy(mapOf(it to this)) as T?
        }
    }