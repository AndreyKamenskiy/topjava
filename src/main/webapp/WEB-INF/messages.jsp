<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty errorMess}">
    <div class="alert">
        <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            ${errorMess}
    </div>
</c:if>

<c:if test="${not empty infoMess}">
    <div class="info">
        <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            ${infoMess}
    </div>
</c:if>
