<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>MyShop Dashboard</title>

    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap"
          rel="stylesheet">

    <style>
        :root { --primary: #4f46e5; --success: #10b981; --danger: #ef4444; --bg: #f8fafc; }

        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Plus Jakarta Sans', sans-serif; }

        body { background: var(--bg); color: #1e293b; }

        .navbar {
            background: #ffffff; padding: 1rem 5%; border-bottom: 1px solid #e2e8f0;
            display: flex; justify-content: space-between; align-items: center;
            position: sticky; top: 0; z-index: 100;
        }

        .navbar h2 { font-weight: 800; color: var(--primary); font-size: 22px; }

        .navbar a {
            text-decoration: none; color: #64748b;
            font-weight: 600; margin-left: 20px; transition: 0.2s;
        }

        .navbar a:hover { color: var(--primary); }

        .container { width: 90%; max-width: 1200px; margin: 40px auto; }

        .user-bar {
            background: white; padding: 20px; border-radius: 16px; margin-bottom: 24px;
            display: flex; justify-content: space-between; align-items: center;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
        }

        .role {
            background: #eef2ff; color: #4f46e5;
            padding: 4px 12px; border-radius: 99px;
            font-size: 11px; font-weight: 700;
            text-transform: uppercase; margin-left: 8px;
        }

        .search-box {
            display: flex; gap: 10px; margin-bottom: 30px;
        }

        .search-box input {
            flex: 1; padding: 14px 20px;
            border: 1px solid #e2e8f0;
            border-radius: 12px; font-size: 15px;
        }

        .admin-box {
            background: #ffffff; padding: 24px;
            border-radius: 16px; margin-bottom: 30px;
            border: 1px solid #e2e8f0;
        }

        .admin-box form {
            display: grid; grid-template-columns: 1fr 1fr 1fr auto;
            gap: 12px;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 24px;
        }

        .card {
            background: white; border-radius: 20px;
            padding: 20px; border: 1px solid #e2e8f0;
            transition: 0.3s; display: flex; flex-direction: column;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 20px 25px -5px rgba(0,0,0,0.05);
        }

        .card h3 {
            font-size: 18px; margin-bottom: 10px; color: #0f172a;
        }

        .price {
            font-size: 20px; color: var(--success);
            font-weight: 800; margin-bottom: 20px;
        }

        .btn {
            padding: 10px 16px; border-radius: 10px;
            font-weight: 700; cursor: pointer;
            border: none; transition: 0.2s;
            text-decoration: none; text-align: center;
            font-size: 14px;
        }

        .btn-primary { background: var(--primary); color: white; }
        .btn-primary:hover { background: #4338ca; }

        .btn-danger { background: #fef2f2; color: var(--danger); }
        .btn-danger:hover { background: var(--danger); color: white; }

        .btn-success { background: var(--success); color: white; }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin-top: 24px;
        }

        .page-link {
            text-decoration: none;
            padding: 8px 12px;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            color: #334155;
            background: #ffffff;
            font-weight: 600;
        }
    </style>
</head>

<body>

<!-- NAVBAR -->
<div class="navbar">

    <h2>🛍️ MyShop</h2>

    <div>

        <c:if test="${role != 'ADMIN'}">
            <a href="Cart.jsp">
                🛒 Cart (${empty sessionScope.cart ? 0 : sessionScope.cart.size()})
            </a>
        </c:if>

        <c:if test="${role == 'ADMIN'}">
            <a href="admin-orders">🔔 Orders</a>
        </c:if>

        <a href="ProductsMain">Home</a>
        <a href="logout">Logout</a>

    </div>

</div>

<!-- SUCCESS MESSAGE -->
<c:if test="${not empty sessionScope.success}">
    <div style="
        background:#ecfdf5;
        color:#065f46;
        padding:12px;
        border-radius:10px;
        margin:20px auto 0;
        font-weight:600;
        width:90%;
        max-width:1200px;
    ">
            ${sessionScope.success}
    </div>
</c:if>

<c:remove var="success" scope="session"/>
<div class="container">

    <!-- USER BAR -->
    <div class="user-bar">

        <div>
            👤 <strong>${username}</strong>
            <span class="role">${role}</span>
        </div>

        <c:if test="${role != 'ADMIN'}">
            <form action="delete-account" method="post">
                <button class="btn btn-danger"
                        onclick="return confirm('Confirm delete?')">
                    Delete Account
                </button>
            </form>
        </c:if>

    </div>

    <!-- ADMIN PANEL -->
    <c:if test="${role == 'ADMIN'}">
        <div class="admin-box">

            <h4 style="margin-bottom: 15px;">Add New Product</h4>

            <form action="add-product" method="post">

                <input type="text" name="name" placeholder="Name" required>
                <input type="number" step="0.01" name="price" placeholder="Price" required>
                <input type="text" name="description" placeholder="Description" required>

                <button class="btn btn-success">Add Product</button>

            </form>

        </div>
    </c:if>

    <!-- SEARCH -->
    <div class="search-box">

        <form action="ProductsMain" method="get"
              style="display:flex;width:100%;gap:10px;">

            <input type="text" name="search"
                   placeholder="Search for products..."
                   value="${search}">

            <button class="btn btn-primary">Search</button>

        </form>

    </div>

    <!-- PRODUCTS -->
    <div class="grid">

        <c:forEach var="item" items="${empty data ? [] : data}">

            <div class="card">

                <h3>${item.name}</h3>

                <div class="price">${item.price} EGP</div>

                <c:choose>

                    <c:when test="${role == 'ADMIN'}">
                        <a href="edit-product?id=${item.id}"
                           class="btn btn-success">
                            Update
                        </a>
                    </c:when>

                    <c:otherwise>
                        <a href="product-details?id=${item.id}"
                           class="btn btn-primary">
                            View Details
                        </a>
                    </c:otherwise>

                </c:choose>

                <c:if test="${role == 'ADMIN'}">

                    <form action="delete-product" method="post" style="margin-top:10px;">

                        <input type="hidden" name="id" value="${item.id}">

                        <button class="btn btn-danger"
                                style="width:100%"
                                onclick="return confirm('Delete product?')">

                            Remove

                        </button>

                    </form>

                </c:if>

                <!-- REVIEW BUTTON -->
                <c:if test="${role == 'ADMIN'}">

                    <a href="product-details?id=${item.id}#reviews"
                       class="btn"
                       style="background:#f1f5f9; color:#0f172a; margin-top:10px;">

                        ⭐ Reviews

                    </a>

                </c:if>
            </div>

        </c:forEach>

    </div>

    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a class="page-link" href="ProductsMain?page=${currentPage - 1}&search=${search}">Previous</a>
            </c:if>

            <span>Page ${currentPage} of ${totalPages}</span>

            <c:if test="${currentPage < totalPages}">
                <a class="page-link" href="ProductsMain?page=${currentPage + 1}&search=${search}">Next</a>
            </c:if>
        </div>
    </c:if>

</div>

</body>
</html>