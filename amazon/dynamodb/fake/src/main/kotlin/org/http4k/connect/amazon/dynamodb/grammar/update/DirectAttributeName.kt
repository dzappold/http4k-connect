package org.http4k.connect.amazon.dynamodb.grammar.update

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.model.AttributeName
import parser4k.Parser
import parser4k.commonparsers.Tokens.identifier
import parser4k.map

object DirectAttributeName : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> = identifier
        .map { value -> Expr { AttributeName.of(value) } }
}
