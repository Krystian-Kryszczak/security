package app.model.security.code.activation

import app.model.security.code.HaveCode

abstract class AccountActivation(
    override val code: String?
): HaveCode
