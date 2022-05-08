package com.tomcat.shoppingServlet;

import com.tomcat.helper.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;

import static com.tomcat.helper.Helper.AESUtil.*;

@WebServlet(name = "shoppingServlet", value = "/shoppingServlet")
public class ShoppingServlet extends HttpServlet {
    private final String[] itemNames = { "糖果", "收音机", "练习薄" };
    private final String TOKEN_PATH = "/WEB-INF/classes/secretKey.properties";
    private final int CACHE_SIZE = 512;
    private final String TOKEN_KEY = "token";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=GBK");

        HttpSession session = request.getSession();
        String token = request.getParameter(TOKEN_KEY);

        ShoppingCart cart = null;
        Cookie tokenCookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(TOKEN_KEY)) {
                    tokenCookie = cookie;
                    // 读取之前保存过的密钥
                    try (Reader reader = new BufferedReader(new FileReader(getServletContext().getRealPath(TOKEN_PATH)), CACHE_SIZE);
                    		Scanner scanner = new Scanner(reader)) 
                    {
						if (scanner.hasNextLine()) {
						    String line = scanner.nextLine();
						    cart = readCookie(tokenCookie.getValue(), base64SecretKeyToSecretKey(line.substring(line.indexOf("=") + 1)));
						    session.setAttribute("cart", cart);
						}
					}
                    break;
                }
            }
        }

        // 适用于首次访问
        if (tokenCookie == null) {
            cart = (ShoppingCart) session.getAttribute("cart");
            if (cart == null) {
                cart = new ShoppingCart();
                session.setAttribute("cart", cart);
            }
        }

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", -1);

        String[] itemsSelected = request.getParameterValues("item");
        boolean isChangeCart = false;

        if (itemsSelected != null) {
            // 防止不点击继续购物链接，直接刷新浏览器后，产生各个商品数量都会自增清空
            assert cart != null;
            if (cart.isTokensEmpty() || !token.equals(cart.getToken())) {
                for (String s : itemsSelected) {
                    cart.add(itemNames[Integer.parseInt(s)]);
                    if (!isChangeCart) isChangeCart = true;
                }
                cart.addToken(token);
            }
        }

        ShoppingCart finalCart = cart;
        boolean finalIsChangeCart = isChangeCart;

        new Helper.HTML(response).generatorHTML(writer -> {
            writer.println("Session ID: " + session.getId() + "<br />");
            assert finalCart != null;
            writer.println("<center><h1>您的购物车有：" + finalCart.getNumberOfItems() + "个商品</h1></center>");
            Map<String, Integer> items = finalCart.getItems();
            writer.println("<ul>");
            for (Map.Entry<String, Integer> entry : items.entrySet())
                writer.println("<li>" + entry.getKey() + ": " + entry.getValue() + "</li>");
            writer.println("</ul>");
            writer.println("<br><a href='shopping.html'>继续购物</a></br>");
            if (finalIsChangeCart) {
                try {
                    writeCookie(request, response, finalCart);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "购物车内容");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private ShoppingCart readCookie(String cookie, SecretKey secretKey) {
        if (cookie != null) {
            try {
                byte[] decrypt = decryptAES(Base64.getDecoder().decode(cookie), secretKey);
                ByteArrayInputStream in = new ByteArrayInputStream(decrypt);
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                return (ShoppingCart) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException | NoSuchPaddingException | IllegalBlockSizeException |
                     NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    private void writeCookie(HttpServletRequest request, HttpServletResponse response, ShoppingCart cart) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        out.writeObject(cart);
        try {
            String base64SecretKey = getSecretKeyAES(SecretKeyLength.SECRET_KEY_128);   // 拿到经过Base64编码后的随机密钥
            SecretKey secretKey = base64SecretKeyToSecretKey(base64SecretKey);  // 把密钥转换成SecretKey对象
            byte[] encrypt = encryptAES(byteArrayOutputStream.toByteArray(), secretKey);    // 加密
            String encryptCart = Base64.getEncoder().encodeToString(encrypt);   // 在响应头中设置Set-Cookie属性

            // 加密密文不需要存储，因为请求这个URL地址时浏览器会把上一次存储的Cookie进行携带请求，只需保存密钥即可
            try (Writer writer = new BufferedWriter(new FileWriter(getServletContext().getRealPath(TOKEN_PATH)), CACHE_SIZE)) {
                writer.write("secretKey=" + base64SecretKey);
            }

            Cookie cookie = new Cookie(TOKEN_KEY, encryptCart);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setHttpOnly(true);
            cookie.setPath(request.getRequestURI());
            cookie.setDomain(request.getServerName());
            response.addCookie(cookie);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
