<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Something went wrong</title>
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background: #f4f6f8;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .error-box {
            background: white;
            padding: 40px;
            border-radius: 18px;
            text-align: center;
            width: 420px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.08);
            animation: fadeIn 0.4s ease-in-out;
        }

        .icon {
            font-size: 60px;
            margin-bottom: 10px;
        }

        h2 {
            margin: 10px 0;
            color: #111827;
        }

        .msg {
            color: #6b7280;
            margin-bottom: 20px;
        }

        .error-detail {
            background: #fef2f2;
            color: #dc2626;
            padding: 10px;
            border-radius: 10px;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .btn {
            display: inline-block;
            padding: 10px 16px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: bold;
            transition: 0.3s;
        }

        .primary {
            background: #2563eb;
            color: white;
        }

        .primary:hover {
            background: #1e40af;
        }

        .secondary {
            background: #e5e7eb;
            color: #111827;
            margin-left: 10px;
        }

        .secondary:hover {
            background: #d1d5db;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-15px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>

<body>

<div class="error-box">

    <div class="icon">🛒</div>

    <h2>Oops! We couldn’t complete your request</h2>

    <p class="msg">Something went wrong while processing your action.</p>

    <div class="error-detail">
        ${error}
    </div>

    <a href="ProductsMain" class="btn primary">Back to Shop</a>
    <a href="javascript:location.reload()" class="btn secondary">Try Again</a>

</div>

</body>
</html>