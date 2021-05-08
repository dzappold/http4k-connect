package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import parser4k.OutputCache
import parser4k.Parser
import parser4k.oneOf
import parser4k.oneOfWithPrecedence
import parser4k.parseWith
import parser4k.reset
import parser4k.with


object UpdateRemoveGrammar {
    private val cache = OutputCache<Expr>()

    fun parse(expression: String): Expr = expression.parseWith(expr)

    private val expr: Parser<Expr> =
        oneOfWithPrecedence(
            UpdateList(::expr).with(cache),
            RemoveAttributeValue(::expr).with(cache),
            RemoveIndexedAttributeValue(::expr).with(cache),
            oneOf(
                ResolveAttributeName(::expr).with(cache),
                DirectAttributeName(::expr).with(cache)
            )
        ).reset(cache)
}


