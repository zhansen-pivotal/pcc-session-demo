package io.pivotal.pcc.util.serializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.PdxWriter;
import org.apache.oltu.oauth2.client.response.OAuthClientResponseFactory;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

@Slf4j
public class OAuthJSONAccessTokenResponseSerializer implements PdxSerializer {
    @Override
    public boolean toData(Object o, PdxWriter out) {
        if (!(o instanceof OAuthJSONAccessTokenResponse)) {
            return false;
        }
        OAuthJSONAccessTokenResponse instance = (OAuthJSONAccessTokenResponse) o;
        out.writeString("body", instance.getBody());
        out.writeInt("responseCode", instance.getResponseCode());
        log.info("custom serialization toData trigger");
        return true;
    }

    @Override
    public Object fromData(Class<?> clazz, PdxReader in) {
        try {
            log.info("custom serialization fromData trigger");
            return OAuthClientResponseFactory
                    .createJSONTokenResponse(
                            in.readString("body"),
                            OAuth.ContentType.JSON,
                            in.readInt("responseCode"));
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            return null;
        }
    }
}