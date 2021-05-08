package org.http4k.connect.amazon.dynamodb.grammar.condition

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import parser4k.Parser
import parser4k.commonparsers.token
import parser4k.inOrder
import parser4k.map
import parser4k.ref
import parser4k.skipWrapper

object Contains : (() -> Parser<Expr>) -> Parser<Expr> {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> =
        inOrder(token("contains"), token("("), ref(parser), token(","), ref(parser), token(")"))
            .skipWrapper()
            .map { (_, attr, _, value) ->
                Expr {
                    when (val av = attr.eval(it).asString()) {
                        is String -> setOf(av)
                        is Set<*> -> av
                        else -> error("can't compare $av")
                    }.contains(value.eval(it).asString().toString())
                }
            }
}
