package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import parser4k.Parser
import parser4k.commonparsers.Tokens.identifier
import parser4k.inOrder
import parser4k.map
import parser4k.oneOf
import parser4k.skipFirst

object ResolveAttributeName : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> = inOrder(oneOf('#'), identifier)
        .skipFirst().map { value ->
            Expr { item ->
                item.names["#$value"] ?: error("no such value #$value")
            }
        }
}
