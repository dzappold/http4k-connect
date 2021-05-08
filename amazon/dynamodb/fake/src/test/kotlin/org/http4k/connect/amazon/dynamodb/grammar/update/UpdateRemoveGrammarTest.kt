package org.http4k.connect.amazon.dynamodb.grammar.update

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.connect.amazon.dynamodb.grammar.ItemWithSubstitutions
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.Item
import org.junit.jupiter.api.Test

class UpdateRemoveGrammarTest {

    private val attrNum = Attribute.int().required("attrNum")
    private val attr1 = Attribute.string().required("attr1")
    private val attrList = Attribute.list().required("attrList")
    private val attrMap = Attribute.map().required("attrMap")

    @Test
    fun `remove value`() {
        assertThat(
            UpdateRemoveGrammar.parse("#attr").eval(
                ItemWithSubstitutions(
                    Item(
                        attr1 of "222",
                        attrNum of 456
                    ),
                    mapOf("#attr" to attr1.name)
                )
            ),
            equalTo(
                Item(
                    attrNum of 456
                )
            )
        )
    }
}
