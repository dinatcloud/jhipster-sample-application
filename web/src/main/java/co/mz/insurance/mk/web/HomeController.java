package co.mz.insurance.mk.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
  public String home() {
    return "forward:index.html";
  }
}
