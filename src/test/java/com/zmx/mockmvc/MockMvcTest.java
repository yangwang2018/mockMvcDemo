package com.zmx.mockmvc;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 钟鸣星
 * @date : 2019年12月12日
 */
@SpringBootTest(classes = {StartUp.class})
@RunWith(SpringRunner.class)
public class MockMvcTest {

    //spring的IOC容器中应该本身存在该bean对象，故可以直接调用
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void init() {
        //利用application对象来创建mockmvc
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * get请求
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        //声明发送一个get请求的方法
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                //无须填写http://localhost:8080之类的域名
                        get("/getUserInfo").
                //添加request的请求参数
                        param("name", "小明").
                        param("addr", "厦门").
                        cookie(new Cookie("token", "USER-881612"));

        //执行请求，perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                //将请求信息和响应信息都打印出来，添加ResultHandler结果处理器，比如调试时打印结果到控制台（对返回的数据进行的判断）
                .andDo(MockMvcResultHandlers.print())
                //添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确（对返回的数据进行的判断），等同于Assert.assertEquals(200,status);
                .andExpect(MockMvcResultMatchers.status().isOk())
                //最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        //响应状态 200 400
        int code = response.getStatus();
        //响应内容
        String result = response.getContentAsString();

    }

    /**
     * post发送json对象
     *
     * @throws Exception
     */
    @Test
    public void testPostJson() throws Exception {
        //做一个json字符串
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("id", "18416523");
        jsonMap.put("name", "手机");
        String json = JSON.toJSONString(jsonMap);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/getOrderInfo").
                        //发送json对象必须要设置请求的文本类型，不然后台无法解析
                        contentType(MediaType.APPLICATION_JSON).
                        //需要调content()来放json字符串，若调用param()来存放json字符串是不可行的，后台无法接收
                        content(json);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        int code = response.getStatus();

        //断言响应的状态是否正常，如果断言成功，不会显示任何结果，如果断言失败，控制台会输出错误结果
        Assert.assertEquals(200, code);
    }

}
