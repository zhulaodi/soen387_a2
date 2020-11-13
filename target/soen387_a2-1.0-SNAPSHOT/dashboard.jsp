<%--
  Created by IntelliJ IDEA.
  User: Ming
  Date: 11/12/2020
  Time: 4:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <title>Hello, world!</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="app">

    <!-- NAV BAR -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand">SOEN387 A2</a>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link">User</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link">_USER_ID</a>
                </li>
                <!-- @ACTION LOGOUT BUTTON -->
                <li class="nav-item">
                    <!-- <a type="button" class="nav-link active">Log out</a>-->
                    <form action="DownloadServlet" method="POST" class="form-login">
                        <input type="submit" name="Logout" value="Sign out" class="btn btn-primary">
                    </form>
                </li>
                <li class="nav-item active dropdown">
                    <a class="nav-link dropdown-toggle"  role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        #
                    </a>
                    <!-- @ACTION DROPDOWN FOR # OF POST IN DASHBOARD -->
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="#">5</a>
                        <a class="dropdown-item" href="#">10</a>
                        <a class="dropdown-item" href="#">20</a>
                    </div>
                </li>
            </ul>
            <!-- @ACTION SEARCH FIELD 1 HASHTAG -->
            <div class="my-2 mr-2 my-lg-0">
                <form onSubmit={handleSearch}>
                    <input type="text" class="form-control form-control-sm" placeholder="search #"/>
                </form>
            </div>
            <!-- @ACTION SEARCH FIELD 2 DATE RANGE -->
            <div class="my-2 mr-2 my-lg-0">
                <form onSubmit={handleSearch}>
                    <input type="text" class="form-control form-control-sm" placeholder="search date range"/>
                </form>
            </div>
            <!-- @ACTION SEARCH FIELD 3 USER ID -->
            <div class="my-2 mr-2 my-lg-0">
                <form onSubmit={handleSearch}>
                    <input type="text" class="form-control form-control-sm" placeholder="user id"/>
                </form>
            </div>
        </div>
    </nav>
    <!-- END OF NAV BAR -->

    <!-- POST CONTAINER -->
    <div class="post_list mx-auto">
        <!-- RENDER MULTIIPLE POST -->
        <div class="post">
            <div class="card">
                <div class="card-body">


                    <!--
                    <ul class="list-group list-group-flush mb-2">
                        <li class="list-group-item">_FILE 1</li>
                        <li class="list-group-item">_FILE 2</li>
                    </ul>
                    -->
                    <c:forEach var="post" items="${posts}">
                        <h4 class="card-title"><strong>_TITLE</strong> <span class="badge badge-secondary">${post.userId}</span></h4>
                        <h6 class="card-subtitle text-muted mb-2">${post.postDate}</h6>
                    <span class="card-text">${post.text}</span>
                        <hr/>
                    </c:forEach>
                    <!--
                    <span class="card-text"> _TEXT"#BML Lorem Ipsum is simply #WDF dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.#LOL"
                  </span>
                    <span class="card-text"> _TEXT"#BML Lorem Ipsum is simply #WDF dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.#LOL"
                  </span>
                  -->
                    <hr/>
                    <div class="owner action">
                        <button class="btn btn-danger mr-2"> DELETE</button>
                        <button class="btn btn-info" data-toggle="modal" data-target="#newPostModal">EDIT</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- FLOAT OBJ -->
    <div class="floatObj">
        <div>
            <span class="d-inline-block" tabIndex="0" data-toggle="tooltip" title="Disabled tooltip">
                <button type="button" class="btn btn-primary" data-toggle="modal"  data-target="#newPostModal">
                    New Post</button>
            </span>
        </div>
    </div>
    <!-- END OF FLOAT OBJ -->

    <!-- NEW POST MODAL -->
    <div class="modal fade" id="newPostModal" tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">NEW POST</h5>
                    <button type="button" class="close" data-dismiss="modal" onClick={handleClose}>
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body ">

                    <div class="FILE mb-2">
                        <input type="file" multiple class="form-control-file" id="exampleFormControlFile1"/>
                    </div>

                    <div class="TITLE mb-2">
                        <input class="form-control" placeHolder="Title"/>
                    </div>

                    <div class="CONTENT">
                        <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" placeHolder="Description"></textarea>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-9">
                                <span class="btn btn-default btn-file">
                                    <input id="input-2" name="input2[]" type="file" class="file" multiple data-show-upload="true" data-show-caption="true"/>
                                </span>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" onClick={handleClose} > Cancel </button>
                    <button type="button" class="btn btn-secondary" onClick={handlePost}> Post </button>
                </div>
            </div>
        </div>
    </div>



</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>

</body>
</html>
