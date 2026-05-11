<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Cart</title>

    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap"
          rel="stylesheet">

    <style>

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Plus Jakarta Sans', sans-serif;
        }

        body {
            background: #f8fafc;
        }

        /* NAVBAR */
        .navbar {
            background: white;
            padding: 18px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #e5e7eb;
        }

        .navbar h2 {
            color: #4f46e5;
            font-weight: 800;
        }

        .navbar a {
            text-decoration: none;
            color: #111827;
            margin-left: 20px;
            font-weight: 600;
        }

        /* CONTAINER */
        .container {
            width: 90%;
            max-width: 1000px;
            margin: 40px auto;
        }

        h1 {
            margin-bottom: 25px;
        }

        /* CART ITEM */
        .item {
            background: white;
            padding: 22px 25px;
            margin-bottom: 18px;
            border-radius: 18px;

            display: flex;
            justify-content: space-between;
            align-items: center;

            box-shadow: 0 6px 20px rgba(0,0,0,0.06);
            transition: 0.2s ease;
        }

        .item:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.08);
        }

        .left {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .title {
            font-size: 18px;
            font-weight: 800;
            color: #0f172a;
        }

        .price {
            color: #10b981;
            font-weight: 800;
        }

        .qty {
            font-size: 14px;
            color: #64748b;
        }

        .controls {
            display: flex;
            gap: 8px;
            margin-top: 8px;
        }

        .qty-btn {
            border: none;
            padding: 6px 10px;
            border-radius: 8px;
            background: #f1f5f9;
            cursor: pointer;
            font-weight: 700;
        }

        .qty-btn:hover {
            background: #e2e8f0;
        }

        .right {
            text-align: right;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .total {
            font-size: 18px;
            font-weight: 800;
        }

        .btn {
            border: none;
            padding: 10px 14px;
            border-radius: 10px;
            cursor: pointer;
            font-weight: 700;
        }

        .primary {
            background: #4f46e5;
            color: white;
        }

        .danger {
            background: #ef4444;
            color: white;
        }

        /* TOTAL BOX */
        .total-box {
            background: white;
            padding: 25px;
            border-radius: 18px;
            margin-top: 25px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.06);
        }

        .empty {
            background: white;
            padding: 50px;
            text-align: center;
            border-radius: 18px;
        }

    </style>

</head>

<body>

<!-- NAVBAR -->
<div class="navbar">
    <h2>🛒 My Cart</h2>

    <div>
        <a href="ProductsMain">Home</a>
        <a href="logout">Logout</a>
    </div>
</div>

<div class="container">

    <!-- SUCCESS -->
    <c:if test="${not empty sessionScope.success}">
        <div style="background:#dcfce7;color:#166534;padding:15px;border-radius:10px;margin-bottom:20px;">
                ${sessionScope.success}
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <!-- ERROR -->
    <c:if test="${not empty sessionScope.error}">
        <div style="background:#fee2e2;color:#991b1b;padding:15px;border-radius:10px;margin-bottom:20px;">
                ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <h1>🛍️ Shopping Cart</h1>

    <!-- EMPTY CART -->
    <c:if test="${empty sessionScope.cart}">
        <div class="empty">
            <h2>Cart is empty 😢</h2>
            <p>Add products first</p>
            <br>
            <a href="ProductsMain">
                <button class="btn primary">Continue Shopping</button>
            </a>
        </div>
    </c:if>

    <!-- CART ITEMS -->
    <c:if test="${not empty sessionScope.cart}">

        <c:set var="total" value="0"/>

        <c:forEach var="item" items="${sessionScope.cart}">

            <div class="item">

                <!-- LEFT -->
                <div class="left">

                    <div class="title">${item.name}</div>

                    <div class="price">${item.price} EGP</div>

                    <div class="qty">Quantity: ${item.quantity}</div>

                    <div class="controls">

                        <form action="update-cart" method="post">
                            <input type="hidden" name="id" value="${item.id}">
                            <input type="hidden" name="action" value="inc">
                            <button class="qty-btn">➕</button>
                        </form>

                        <form action="update-cart" method="post">
                            <input type="hidden" name="id" value="${item.id}">
                            <input type="hidden" name="action" value="dec">
                            <button class="qty-btn">➖</button>
                        </form>

                    </div>

                </div>

                <!-- RIGHT -->
                <div class="right">

                    <div class="total">${item.total} EGP</div>

                    <form action="remove-from-cart" method="post">
                        <input type="hidden" name="id" value="${item.id}">
                        <button class="btn danger">Remove</button>
                    </form>

                </div>

            </div>

            <c:set var="total" value="${total + item.total}"/>

        </c:forEach>

        <!-- TOTAL -->
        <div class="total-box">

            <h2>💰 Total: ${total} EGP</h2>

            <br>

            <form action="checkout" method="post">
                <button class="btn primary">Place Order</button>
            </form>

            <form action="clear-cart" method="post" style="margin-top:10px;">
                <button class="btn danger">Clear Cart</button>
            </form>

        </div>

    </c:if>

</div>

</body>

</html>