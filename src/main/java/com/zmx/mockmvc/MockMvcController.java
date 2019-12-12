package com.zmx.mockmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author : 钟鸣星
 * @date : 2019年12月12日
 */
@RestController
public class MockMvcController {

    private Logger log = LoggerFactory.getLogger(MockMvcController.class);

    @RequestMapping("/getUserInfo")
    public String getUserInfo(String name, String addr, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String cookieName = cookies[0].getName();
        String cookieValue = cookies[0].getValue();
        log.info("name={},addr={},cookieName={},cookieValue={}", name, addr, cookieName, cookieValue);

        return "name=" + name + "; addr=" + addr + "; cookieName=" + cookieName + "; cookieValue=" + cookieValue;
    }

    @RequestMapping("/getOrderInfo")
    public Order getOrder(@RequestBody Order order){
       log.info("order--->" + order);
       return order;
    }
}
