<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Product</title>

    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap"
          rel="stylesheet">

    <style>
        :root {
            --primary:#4f46e5;
            --bg:#f8fafc;
            --text:#0f172a;
            --muted:#64748b;
        }

        * {
            box-sizing:border-box;
            font-family:'Plus Jakarta Sans', sans-serif;
        }

        body {
            margin:0;
            background:var(--bg);
            display:flex;
            justify-content:center;
            align-items:center;
            min-height:100vh;
        }

        .card {
            width:420px;
            background:white;
            border-radius:20px;
            padding:30px;
            box-shadow:0 15px 35px rgba(0,0,0,0.08);
        }

        h2 {
            margin-bottom:20px;
            color:var(--text);
            font-size:24px;
            font-weight:800;
        }

        label {
            font-size:13px;
            color:var(--muted);
            font-weight:600;
            display:block;
            margin-bottom:6px;
        }

        input {
            width:100%;
            padding:12px 14px;
            margin-bottom:16px;
            border-radius:12px;
            border:1px solid #e2e8f0;
            outline:none;
            transition:0.2s;
        }

        input:focus {
            border-color:var(--primary);
            box-shadow:0 0 0 3px rgba(79,70,229,0.1);
        }

        button {
            width:100%;
            padding:12px;
            background:var(--primary);
            color:white;
            border:none;
            border-radius:12px;
            font-weight:700;
            cursor:pointer;
            transition:0.2s;
        }

        button:hover {
            background:#4338ca;
        }

        .hint {
            font-size:12px;
            color:var(--muted);
            margin-bottom:15px;
        }
    </style>
</head>

<body>

<div class="card">

    <h2>✏️ Edit Product</h2>
    <div class="hint">Update product details below</div>

    <form action="update-product" method="post">

        <input type="hidden" name="id" value="${product.id}">

        <label>Name</label>
        <input type="text" name="name" value="${product.name}" required>

        <label>Price</label>
        <input type="number" step="0.01" name="price" value="${product.price}" required>

        <label>Description</label>
        <textarea name="description" required
                  style="
              width:100%;
              min-height:120px;
              padding:12px 14px;
              border-radius:12px;
              border:1px solid #e2e8f0;
              outline:none;
              resize:vertical;
              font-family:'Plus Jakarta Sans', sans-serif;
              transition:0.2s;
          "
                  onfocus="this.style.borderColor='#4f46e5'; this.style.boxShadow='0 0 0 3px rgba(79,70,229,0.1)'"
                  onblur="this.style.borderColor='#e2e8f0'; this.style.boxShadow='none'">${product.description}</textarea>

        <button type="submit">Update Product</button>

    </form>

</div>

</body>
</html>