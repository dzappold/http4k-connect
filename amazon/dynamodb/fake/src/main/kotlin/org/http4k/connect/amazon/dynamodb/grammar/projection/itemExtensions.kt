package org.http4k.connect.amazon.dynamodb.grammar.projection

import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.grammar.condition.AttributeNameValue
import org.http4k.connect.amazon.dynamodb.model.AttributeName
import org.http4k.connect.amazon.dynamodb.model.AttributeValue
import org.http4k.connect.amazon.dynamodb.model.Item
import org.http4k.connect.amazon.dynamodb.model.TokensToNames

/**
 * Transform the input item by applying the projection to the fields in it.
 */
fun Item.project(
    projectionExpression: String?,
    expressionAttributeNames: TokensToNames?
): Item =
    when (projectionExpression) {
        null -> this
        else -> {
            val item = ItemWithSubstitutions(this, expressionAttributeNames ?: emptyMap())
            val allItems: List<AttributeNameValue> = projectionExpression.split(',')
                .map(String::trim)
                .map(ProjectionGrammar::parse)
                .flatMap { it.eval(item) as List<AttributeNameValue> }

            allItems
                .groupBy { it.first }
                .mapValues { it.value.map { it.second } }
                .map { (name: AttributeName, values: List<AttributeValue>) ->
                    name to when {
                        values[0].L != null -> AttributeValue.List(values.flatMap { it.L!! })
                        values[0].M != null -> AttributeValue.Map(values
                            .map { it.M!! }
                            .fold(Item()) { acc, next -> acc + next })
                        else -> values[0]
                    }
                }.toMap()
        }
    }
