<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>${product.name} | Details</title>

  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">

  <style>
    :root { --primary:#4f46e5; --bg:#f8fafc; --slate-900:#0f172a; }

    * { box-sizing:border-box; font-family:'Plus Jakarta Sans',sans-serif; }

    body { background:var(--bg); margin:0; padding-bottom:50px; }

    .container { width:90%; max-width:1000px; margin:40px auto; }

    .product-hero {
      background:white;
      border-radius:24px;
      padding:40px;
      display:grid;
      grid-template-columns:1fr 1fr;
      gap:40px;
      box-shadow:0 10px 25px rgba(0,0,0,0.05);
      align-items:center;
    }

    .img-large {
      background:#f1f5f9;
      border-radius:20px;
      height:350px;
      display:flex;
      align-items:center;
      justify-content:center;
      font-size:80px;
    }

    h1 { font-size:32px; margin-bottom:10px; }

    .price { font-size:28px; color:#10b981; font-weight:800; margin-bottom:20px; }

    .desc { color:#64748b; line-height:1.7; margin-bottom:20px; }

    .btn {
      padding:12px 24px;
      border-radius:10px;
      font-weight:700;
      border:none;
      cursor:pointer;
    }

    .btn-primary { background:var(--primary); color:white; }

    .section {
      background:white;
      border-radius:20px;
      padding:30px;
      margin-top:30px;
    }

    .review-item {
      padding:20px 0;
      border-bottom:1px solid #f1f5f9;
    }

    .stars { color:#f59e0b; font-weight:700; }

    .review-user { font-weight:700; }

    .review-text { color:#475569; margin-top:5px; }

    .back-link {
      display:inline-block;
      margin-top:30px;
      color:var(--primary);
      font-weight:700;
      text-decoration:none;
    }
  </style>
</head>

<body>
<!-- TOP NAVBAR -->
<div style="
  background:white;
  padding:16px 5%;
  display:flex;
  justify-content:space-between;
  align-items:center;
  border-bottom:1px solid #e2e8f0;
  position:sticky;
  top:0;
  z-index:999;
">

  <!-- BRAND -->
  <div style="font-weight:800; font-size:20px; color:#4f46e5;">
    🛍️ MyShop
  </div>

  <!-- LINKS -->
  <div style="display:flex; gap:20px; align-items:center;">

    <a href="ProductsMain" style="text-decoration:none; color:#64748b; font-weight:600;">
      Home
    </a>

    <c:if test="${role != 'ADMIN'}">
      <a href="Cart.jsp" style="text-decoration:none; color:#64748b; font-weight:600;">
        🛒 Cart (${empty sessionScope.cart ? 0 : sessionScope.cart.size()})
      </a>
    </c:if>

    <a href="logout" style="text-decoration:none; color:#ef4444; font-weight:700;">
      Logout
    </a>

  </div>

</div>

<div class="container">

  <div class="product-hero">

    <div class="img-large">🛍️</div>

    <div>

      <h1>${product.name}</h1>

      <div class="price">${product.price} EGP</div>

      <p class="desc">${product.description}</p>

      <!-- ADD TO CART (SAFE) -->
      <c:if test="${role != 'ADMIN'}">

        <form action="add-to-cart" method="post">

          <input type="hidden" name="id" value="${product.id}">
          <input type="hidden" name="name" value="${product.name}">
          <input type="hidden" name="price" value="${product.price}">

          <button type="submit" class="btn btn-primary">
            🛒 Add to Cart
          </button>

        </form>

      </c:if>

    </div>

  </div>

  <!-- REVIEWS -->
  <div class="section">

    <h2 style="margin-bottom:20px;">
      Customer Reviews
      (
      ${empty reviews ? 0 : reviews.size()}
      )
    </h2>

    <!-- ADD REVIEW (ONLY USER) -->
    <c:if test="${role != 'ADMIN'}">

      <div style="
        margin:20px 0;
        padding:20px;
        background:#f8fafc;
        border-radius:14px;
        border:1px solid #e2e8f0;
    ">

        <form action="add-review" method="post" style="display:flex; gap:10px; flex-wrap:wrap; align-items:center;">

          <input type="hidden" name="productId" value="${product.id}">

          <input type="number" name="rating" min="1" max="5"
                 placeholder="⭐ Rating (1-5)"
                 style="padding:10px; border:1px solid #e2e8f0; border-radius:10px; width:150px;"
                 required>

          <input type="text" name="comment"
                 placeholder="Write your review..."
                 style="flex:1; padding:10px; border:1px solid #e2e8f0; border-radius:10px;"
                 required>

          <button type="submit" class="btn btn-primary">
            Add Review
          </button>

        </form>

      </div>

    </c:if>

    <!-- REVIEWS LIST -->
    <div style="display:flex; flex-direction:column; gap:15px;">

      <c:forEach var="review" items="${empty reviews ? [] : reviews}">

        <div class="review-item"
             style="
              background:white;
              padding:18px;
              border-radius:14px;
              border:1px solid #e2e8f0;
              box-shadow:0 2px 6px rgba(0,0,0,0.03);
           ">

          <!-- HEADER -->
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;">

            <div class="review-user" style="font-size:15px;">
              👤 ${review.username}
            </div>

            <div class="stars" style="font-size:14px;">
              ⭐ ${review.rating}/5
            </div>

          </div>

          <!-- COMMENT -->
          <div class="review-text" style="color:#475569; font-size:14px;">
              ${review.comment}
          </div>

          <!-- ADMIN DELETE -->
          <c:if test="${role == 'ADMIN'}">

            <form action="delete-review" method="post" style="margin-top:10px; text-align:right;">

              <input type="hidden" name="reviewId" value="${review.id}">
              <input type="hidden" name="productId" value="${product.id}">

              <button class="btn"
                      style="color:#ef4444; background:#fef2f2; padding:6px 12px; font-size:12px;">
                Delete
              </button>

            </form>

          </c:if>

        </div>

      </c:forEach>

    </div>

  </div>

  <a href="ProductsMain" class="back-link">← Return to Catalog</a>

</div>

</body>
</html>