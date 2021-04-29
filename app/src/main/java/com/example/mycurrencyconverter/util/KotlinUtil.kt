package com.example.mycurrencyconverter.util

fun <A, B> bothNonNull(a: A?, b: B?): Pair<A, B>? = if (a != null && b != null) Pair(a, b) else null