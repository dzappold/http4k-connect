package org.http4k.connect.amazon.dynamodb.grammar.condition

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.model.AttributeValue

internal const val NULLMARKER = "__*NULL*__"

internal fun ItemWithSubstitutions.comparable(expr: Expr) = expr.eval(this).asString().toString().padStart(200)

internal fun Any.asString(): Any =
    with(this as AttributeValue) {
        when {
            B != null -> B!!.value
            BOOL != null -> BOOL!!.toString()
            BS != null -> BS!!.map { it.value }
            L != null -> L!!.map(Any::asString)
            M != null -> M!!.map { it.value.asString() }
            N != null -> N!!
            NS != null -> NS!!
            S != null -> S!!
            SS != null -> SS!!
            else -> NULLMARKER
        }
    }
