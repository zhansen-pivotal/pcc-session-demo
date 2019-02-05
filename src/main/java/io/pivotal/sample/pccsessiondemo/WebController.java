package io.pivotal.sample.pccsessiondemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@SuppressWarnings("unused")
public class WebController {
  public static final AtomicLong counter = new AtomicLong(1);

  @GetMapping("/ping")
  public String ping() {
    return "PONG";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/")
  public String homepage(HttpSession session) {
    System.out.println("homepage : SessionId = " + session.getId());
    System.out.println("Session IsNew = " + session.isNew());
    counter.getAndIncrement();
    session.setAttribute("counter_value", counter);
    printSessionAttributes("homepage", session);
    return "session id - " + session.getId() + " : counter " + session.getAttribute(
            "counter_value");
  }

  @SuppressWarnings("all")
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

