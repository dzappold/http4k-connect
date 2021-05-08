package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import parser4k.OutputCache
import parser4k.Parser
import parser4k.oneOf
import parser4k.oneOfWithPrecedence
import parser4k.parseWith
import parser4k.reset
import parser4k.with


object UpdateAddGrammar {
    private val cache = OutputCache<Expr>()

    fun parse(expression: String): Expr = expression.parseWith(expr)

    private val expr: Parser<Expr> =
        oneOfWithPrecedence(
            UpdateList(::expr).with(cache),
            oneOf(
                UpdateExpressionAttributeName(::expr).with(cache),
                RemoveAttributeValue(::expr).with(cache)
            )
        ).reset(cache)
}


