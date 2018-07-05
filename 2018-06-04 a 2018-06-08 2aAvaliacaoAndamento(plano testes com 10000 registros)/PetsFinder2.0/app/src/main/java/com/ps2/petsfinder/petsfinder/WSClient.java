package com.ps2.petsfinder.petsfinder;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WSClient {

    public boolean testaUsuarioSenha(String usuario, String senha) throws IOException, XmlPullParserException {
        SoapObject soap = new SoapObject("http://webservice", "testaUsuarioSenha");
        soap.addProperty("usuario", usuario);
        soap.addProperty("senha", senha);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);
        HttpTransportSE http = new HttpTransportSE("https://calm-sea-53138.herokuapp.com/helloworld/services/WebService?wsdl");
        http.call("testaUsuarioSenha", envelope);

        Object resposta = envelope.getResponse();
        if(resposta.toString().equalsIgnoreCase("true"))
            return true;
        else
            return false;
    }
}
