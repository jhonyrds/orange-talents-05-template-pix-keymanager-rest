package br.com.zup.request

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Nested
class TipoDeChaveRequestTest {

    @Test
    fun `deve gerar chave aleatoria quando o campo chave for nulo ou vazio`() {

        //cenário

        //ação
        val chaveAleatoria = TipoDeChaveRequest.ALEATORIA

        //validação
        assertTrue(chaveAleatoria.validaChave(null))
        assertTrue(chaveAleatoria.validaChave(""))
    }

    @Test
    fun `nao deve gerar a chave aleatoria quando for informado algum valor no campo`() {

        //cenário

        //ação
        val chaveAleatoria = TipoDeChaveRequest.ALEATORIA

        //validação
        assertFalse(chaveAleatoria.validaChave("qualquer coisa"))
        assertFalse(chaveAleatoria.validaChave("0123456789"))
    }

    @Test
    fun `deve gerar a chave quando for informado um cpf valido`() {

        //cenário

        //ação
        val chavecComCpf = TipoDeChaveRequest.CPF

        //validação
        assertTrue(chavecComCpf.validaChave("89716282788"))

    }

    @Test
    fun `nao deve gerar a chave quando o cpf nao for informado ou for nulo`() {

        //cenario

        //ação
        val chavecComCpf = TipoDeChaveRequest.CPF

        //validação
        assertFalse(chavecComCpf.validaChave(null))
        assertFalse(chavecComCpf.validaChave(""))
    }

    @Test
    fun `nao deve gerar a chave quando o cpf for invalido`() {

        //cenário

        //ação
        val chavecComCpf = TipoDeChaveRequest.CPF

        //validação
        assertFalse(chavecComCpf.validaChave("qualquer coisa"))
    }

    @Test
    fun `deve gerar a chave quando for informado um numero de celular valido`() {

        //cenário

        //ação
        val chaveComCelular = TipoDeChaveRequest.CELULAR

        //validação
        assertTrue(chaveComCelular.validaChave("+5511999999999"))
    }

    @Test
    fun `nao deve gerar chave quando o campo celular for nulo ou vazio`() {

        //cenário

        //ação
        val chaveComCelular = TipoDeChaveRequest.CELULAR

        //validação
        assertFalse(chaveComCelular.validaChave(null))
        assertFalse(chaveComCelular.validaChave(""))
    }

    @Test
    fun `nao deve gerar chave quando o campo celular for um formato invalido`() {

        //cenário

        //ação
        val chaveComCelular = TipoDeChaveRequest.CELULAR

        //validação
        assertFalse(chaveComCelular.validaChave("11999999999"))
        assertFalse(chaveComCelular.validaChave("999999999"))
        assertFalse(chaveComCelular.validaChave("99999-9999"))
    }

    @Test
    fun `deve gerar chave quando for informado um e-mail valido`() {

        //cenário

        //ação
        val chaveComEmail = TipoDeChaveRequest.EMAIL

        //validação
        assertTrue(chaveComEmail.validaChave("email@teste.com.br"))

    }

    @Test
    fun `nao deve gerar chave quando o campo e-mail for nulo ou vazio`() {

        //cenário

        //ação
        val chaveComEmail = TipoDeChaveRequest.EMAIL

        //validação
        assertFalse(chaveComEmail.validaChave(null))
        assertFalse(chaveComEmail.validaChave(""))
    }

    @Test
    fun `nao deve gerar chave quando for informado um -email invalido`() {

        //cenário

        //ação
        val chaveComEmail = TipoDeChaveRequest.EMAIL

        //validação
        assertFalse(chaveComEmail.validaChave("qualquer coisa"))
        assertFalse(chaveComEmail.validaChave("email@"))
        assertFalse(chaveComEmail.validaChave("emailteste.com.br"))
        assertFalse(chaveComEmail.validaChave("emailteste12345"))
    }

}