<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register for Event</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <div class="card p-4" style="max-width: 600px; margin: auto;">
        <h1 class="mb-3">Confirm Registration</h1>
        <h2 class="h4">${event.title}</h2>
        <hr>
        <p><strong>Date:</strong> ${event.dateTime}</p>
        <p><strong>Location:</strong> ${event.location}</p>
        <p><strong>Price:</strong>
            <c:choose>
                <c:when test="${event.price == 0}">Free</c:when>
                <c:otherwise>${event.price} €</c:otherwise>
            </c:choose>
        </p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="/WebTechProject/events/${event.id}/register">
            <button type="submit" class="btn btn-primary w-100">Confirm Registration</button>
        </form>

        <a href="/WebTechProject/events/${event.id}" class="btn btn-outline-secondary w-100 mt-2">
            ← Back to event
        </a>
    </div>
</div>
</body>
</html>