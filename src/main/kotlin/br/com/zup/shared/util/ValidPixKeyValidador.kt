package br.com.zup.shared.util

import br.com.zup.request.ChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton

@Singleton
class ValidPixKeyValidador : ConstraintValidator<ValidPixKey, ChavePixRequest> {

    override fun isValid(
        value: ChavePixRequest,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {

        if (value.tipoDeChave == null) {
            return false
        }
        return value.tipoDeChave.validaChave(value.chave!!)
    }
}
