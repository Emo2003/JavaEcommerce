<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Welcome Back | MyShop</title>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
  <style>
    :root { --primary: #4f46e5; --primary-dark: #4338ca; --slate: #1e293b; --text-light: #64748b; }
    * { box-sizing: border-box; font-family: 'Plus Jakarta Sans', sans-serif; }
    body { margin: 0; background: #f8fafc; display: flex; justify-content: center; align-items: center; min-height: 100vh; }

    .container {
      display: flex; width: 900px; height: 550px; background: white;
      border-radius: 24px; box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.1); overflow: hidden;
    }

    .left {
      flex: 1; background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);
      color: white; padding: 60px; display: flex; flex-direction: column; justify-content: center;
    }
    .left h1 { font-size: 32px; font-weight: 800; margin-bottom: 15px; }
    .left p { font-size: 18px; opacity: 0.9; line-height: 1.6; }

    .right { flex: 1; padding: 60px; display: flex; flex-direction: column; justify-content: center; }
    h2 { font-size: 24px; color: var(--slate); margin-bottom: 8px; }
    .subtitle { color: var(--text-light); margin-bottom: 30px; font-size: 14px; }

    .error {
      background: #fef2f2; color: #dc2626; padding: 12px; border-radius: 10px;
      font-size: 13px; margin-bottom: 20px; border: 1px solid #fee2e2;
    }

    input {
      width: 100%; padding: 14px 16px; margin-bottom: 16px; border: 1px solid #e2e8f0;
      border-radius: 12px; outline: none; transition: 0.2s; background: #fcfcfd;
    }
    input:focus { border-color: var(--primary); box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.1); background: white; }

    button {
      width: 100%; padding: 14px; background: var(--primary); color: white; border: none;
      border-radius: 12px; font-weight: 700; cursor: pointer; transition: 0.3s;
    }
    button:hover { background: var(--primary-dark); transform: translateY(-1px); box-shadow: 0 10px 15px -3px rgba(79, 70, 229, 0.3); }

    .link { text-align: center; margin-top: 25px; font-size: 14px; color: var(--text-light); }
    .link a { color: var(--primary); text-decoration: none; font-weight: 700; }

    @media (max-width: 850px) { .left { display: none; } .container { width: 400px; } }
  </style>
</head>
<body>
<div class="container">
  <div class="left">
    <h1>🛍️ MyShop</h1>
    <p>Experience the next generation of shopping with curated products just for you.</p>
  </div>
  <div class="right">
    <h2>Welcome Back</h2>
    <p class="subtitle">Please enter your details to sign in.</p>
    <div class="error">${error}</div>
    <form method="post" action="login">
      <input type="text" name="username" placeholder="Username" required />
      <input type="password" name="password" placeholder="Password" required />
      <button type="submit">Sign In</button>
    </form>
    <div class="link">
      Don't have an account? <a href="signup.jsp">Create one</a>
    </div>
  </div>
</div>
</body>
</html>