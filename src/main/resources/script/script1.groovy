
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;

def Message processData(Message message) {
    //Recupera o Body da Mensagem
    def body = message.getBody(java.lang.String);
    
    // instancia do XML Slurper
    def xml = new XmlSlurper().parseText(body)
    
    // Tratativa 1. Alterar o apelido do cadastro
    xml.cadastro.pessoa.apelido.replaceNode {
        apelido('Carlinhos')
    }

    // Tratativa 2 Adicionar pais Brasil no endereço        
    xml.cadastro.endereco.appendNode {
        pais("Brasil")
    }

    // Tratativa 3 Remover o TelefoneFixo de todos os contatos
    def contatosTelefoneFixo = xml.cadastro.contatos.depthFirst().findAll { it.contato.@tipo == "TelefoneFixo" }
    for (contato in contatosTelefoneFixo){
        contato.replaceNode {}
    }
    
    // Tratativa 4 Adicionar atributo codpais = 55 nos contatos de Telefone
    def contatosTelefone = xml.cadastro.contatos.depthFirst().findAll { it.contato.@tipo.toString().indexOf('Telefone') > -1 }
    for (contato in contatosTelefone){
        contato.contato.@codpais = '55'
    }


    def String xmlSaida = groovy.xml.XmlUtil.serialize(xml)
    message.setBody(xmlSaida);      
    
    return message;
}