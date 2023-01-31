package app.model

import java.util.UUID

abstract class NamedItem(
    id: UUID? = null,
    override var name: String? = null,
): Item(id), Named
