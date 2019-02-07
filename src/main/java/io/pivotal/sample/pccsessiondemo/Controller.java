package io.pivotal.sample.pccsessiondemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.geode.cache.Cache;
import org.apache.geode.pdx.PdxSerializer;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.gemfire.serialization.pdx.support.ComposablePdxSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class Controller {
    public static final AtomicLong counter = new AtomicLong(1);

    @Autowired
    Cache cache;

    @Autowired
    Service service;

    @GetMapping("/ping")
    public String ping() {
        return "PONG";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String homepage(HttpSession session) {

        PdxSerializer serializer = cache.getPdxSerializer();
        if (serializer instanceof ComposablePdxSerializer) {
            log.info("composable pdx serializer");
            ComposablePdxSerializer composablePdxSerializer = (ComposablePdxSerializer) serializer;
            composablePdxSerializer.iterator().forEachRemaining(s -> log.info(s.getClass().getName()));
        } else {
            log.info("not composable pdx serializer: " + serializer.getClass().getName());
        }

        System.out.println("homepage : SessionId = " + session.getId());
        System.out.println("Session IsNew = " + session.isNew());
        counter.getAndIncrement();
        session.setAttribute("counter_value", counter);
        OAuthJSONAccessTokenResponse oAuthJSONAccessTokenResponse = new OAuthJSONAccessTokenResponse();
        session.setAttribute("oAuthJSONAccessTokenResponse", oAuthJSONAccessTokenResponse);

        Entity entity = service.createNewEntity();
        session.setAttribute("entity", service.saveEntity(entity));
        session.setAttribute("cacheable", service.getEntity(entity.getId()));

        printSessionAttributes("homepage", session);
        return "session id - " + session.getId() + " : counter " + session.getAttribute(
                "counter_value");
    }

    private HttpSession printSessionAttributes(String label, HttpSession session) {
        Enumeration<String> attributeNames = session.getAttributeNames();
        if (attributeNames != null) {
            while (attributeNames.hasMoreElements()) {
                String sessionAttributeKey = attributeNames.nextElement();
                Object sessionAttributeValue = session.getAttribute(sessionAttributeKey);
                System.out.printf("%s { [%s] = [%s] }%n", label, sessionAttributeKey, sessionAttributeValue);
            }
        }
        return session;
    }
}

