package org.http4k.connect.amazon.dynamodb.grammar.condition

import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.model.Item
import org.http4k.connect.amazon.dynamodb.model.TokensToNames
import org.http4k.connect.amazon.dynamodb.model.TokensToValues

/**
 * Apply the conditional expression to the Item. If the condition is null or resolves to true returns the item,
 * or returns null.
 */
fun Item.condition(
    expression: String?,
    expressionAttributeNames: TokensToNames?,
    expressionAttributeValues: TokensToValues?
) = when (expression) {
    null -> this
    else -> takeIf {
        ConditionGrammar.parse(expression).eval(
            ItemWithSubstitutions(
                this,
                expressionAttributeNames ?: emptyMap(),
                expressionAttributeValues ?: emptyMap()
            )
        ) == true
    }
}
