package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.model.AttributeName
import parser4k.Parser
import parser4k.commonparsers.Tokens
import parser4k.map

object RemoveAttribute : (() -> Parser<Expr>) -> Parser<Expr> {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> = Tokens.identifier.map {
        Expr { item -> item.item - AttributeName.of(it) }
    }
}
