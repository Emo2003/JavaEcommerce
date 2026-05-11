<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <title>Orders</title>

  <style>
    body { font-family: Arial; background: #f8fafc; margin:0; }

    /* NAVBAR */
    .navbar {
      background: #ffffff;
      padding: 15px 5%;
      border-bottom: 1px solid #e2e8f0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .navbar h2 {
      color: #4f46e5;
      margin: 0;
    }

    .navbar a {
      text-decoration: none;
      color: #64748b;
      font-weight: 600;
      margin-left: 15px;
    }

    .navbar a:hover {
      color: #4f46e5;
    }

    /* TABLE DESIGN (UNCHANGED) */
    table {
      width: 100%;
      border-collapse: collapse;
      background: white;
    }

    th, td {
      padding: 15px;
      border: 1px solid #ddd;
    }

    .btn {
      padding: 8px 12px;
      border: none;
      border-radius: 8px;
      color: white;
      cursor: pointer;
    }

    .accept { background: #10b981; }
    .reject { background: #ef4444; }
  </style>
</head>

<body>

<!-- NAVBAR -->
<div class="navbar">

  <h2>🛍️ MyShop</h2>

  <div>
    <a href="ProductsMain">Home</a>
    <a href="logout">Logout</a>
  </div>

</div>

<h2>📦 Orders</h2>

<c:if test="${empty orders}">
  <p>No orders found.</p>
</c:if>

<table>

  <tr>
    <th>User</th>
    <th>Total</th>
    <th>Status</th>
    <th>Action</th>
  </tr>

  <c:forEach var="o" items="${orders}">

    <tr>
      <td>${o.username}</td>
      <td>${o.total}</td>
      <td>${o.status}</td>

      <td>
        <form action="update-order" method="post">

          <input type="hidden" name="id" value="${o.id}"/>

          <button name="status" value="ACCEPTED" class="btn accept">
            Accept
          </button>

          <button name="status" value="REJECTED" class="btn reject">
            Reject
          </button>

        </form>
      </td>
    </tr>

  </c:forEach>

</table>

</body>
</html>