package com.rezerwacja_stolikow.util

infix fun <A, B, C> Pair<A, B>.with(that: C) = Triple(this.first, this.second, that)
