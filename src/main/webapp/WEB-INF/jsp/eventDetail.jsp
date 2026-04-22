<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${event.title}</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <a href="/WebTechProject/events" class="btn btn-outline-secondary mb-4">← Back to events</a>
    <div class="card p-4">
        <h1>${event.title}</h1>
        <p class="text-muted">${event.description}</p>
        <hr>
        <p><strong>Date:</strong> ${event.dateTime}</p>
        <p><strong>Location:</strong> ${event.location}</p>
        <p><strong>Capacity:</strong> ${event.capacity} people</p>
        <p><strong>Price:</strong>
            <c:choose>
                <c:when test="${event.price == 0}">Free</c:when>
                <c:otherwise>${event.price} €</c:otherwise>
            </c:choose>
        </p>
        <span class="badge ${event.virtual ? 'bg-info' : 'bg-success'}">
            ${event.virtual ? 'Virtual' : 'In-person'}
        </span>

        <%-- ↓ REEMPLAZA el <div class="mt-4"> que tenías con esto ↓ --%>
        <div class="mt-4">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${param.registered == 'true'}">
                            <div class="alert alert-success">
                                ✓ You are registered for this event!
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="/WebTechProject/events/${event.id}/register"
                               class="btn btn-primary">
                                Register for this event
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <p>
                        <a href="/WebTechProject/login">Log in</a>
                        to register for this event.
                    </p>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</div>
</body>
</html>