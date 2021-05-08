package org.http4k.connect.amazon.dynamodb.grammar.projection

import org.http4k.connect.amazon.dynamodb.grammar.Expr
import org.http4k.connect.amazon.dynamodb.grammar.ExprFactory
import org.http4k.connect.amazon.dynamodb.grammar.condition.AttributeNameValue
import org.http4k.connect.amazon.dynamodb.model.AttributeName
import org.http4k.connect.amazon.dynamodb.model.AttributeValue
import parser4k.Parser
import parser4k.commonparsers.Tokens.identifier
import parser4k.map


object ProjectionAttributeValue : ExprFactory {
    override operator fun invoke(parser: () -> Parser<Expr>): Parser<Expr> = identifier.map(::ProjectionAttributeValue)
}

fun ProjectionAttributeValue(value: String) = Expr { item ->
    listOf(
        AttributeNameValue(
            AttributeName.of(value),
            (item.item[AttributeName.of(value)] ?: AttributeValue.Null())
        )
    )
}
