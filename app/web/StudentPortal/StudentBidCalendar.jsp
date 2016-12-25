<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="panel panel-inverse">
    <div class="panel-heading">
        <div class="panel-heading-btn">
            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-default" data-click="panel-expand"><i class="fa fa-expand"></i></a>
            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-success" data-click="panel-reload"><i class="fa fa-repeat"></i></a>
            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-warning" data-click="panel-collapse"><i class="fa fa-minus"></i></a>
            <a href="javascript:;" class="btn btn-xs btn-icon btn-circle btn-danger" data-click="panel-remove"><i class="fa fa-times"></i></a>
        </div>
        <h4 class="panel-title">Exam & Class Dates Calendar</h4>
    </div>
    <div class="panel-body p-0">
        <div class="vertical-box">
            <div class="vertical-box-column p-15 bg-silver width-sm">
                <div id="external-events" class="calendar-event">
                    <h4 class=" m-b-20">Modules</h4>
                    <div class="external-event bg-blue" data-bg="bg-blue" data-title="Dinner" data-media="<i class='fa fa-cutlery'></i>" data-desc="Cum sociis natoque penatibus et magnis dis parturient montes.">
                        <h5><i class="fa fa-cutlery fa-lg fa-fw"></i> Exams</h5>
                        <p>Blue blocks will represent exams</p>
                    </div>
                    <div class="external-event bg-green" data-bg="bg-green" data-title="Brainstorming" data-media="<i class='fa fa-lightbulb-o'></i>" data-desc="Mauris tristique massa eu venenatis semper. Phasellus a nibh nisi.">
                        <h5><i class="fa fa-lightbulb-o fa-lg fa-fw"></i> Classes</h5>
                        <p>Green blocks will represent classes</p>
                    </div>
                </div>
            </div>
            <div id="calendar" class="vertical-box-column p-15 calendar"></div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        
        function initCalendar() {

            var e = {
                left: "prevYear,nextYear today prev,next ",
                center: "title",
                right: "month,agendaWeek,agendaDay"
            };
            var t = new Date;
            var n = t.getMonth();
            var r = t.getFullYear();
            var i = $("#calendar").fullCalendar({
                header: e,
                selectHelper: true,
                eventRender: function (event, element) {
                    var r = event.media ? event.media : "";
                    var i = event.description ? event.description : "";
                    console.log(element.find(".fc-title"));
                    element.find(".fc-title").after($('<span class="fc-event-icons"></span>').html(r));
                    element.find(".fc-title").append("<small>" + i + "</small>")
                },
                editable: true,
                events: function (start, end, timezone, callback) {
                    var baseURL = window.location.pathname.substring(0, window.location.pathname.lastIndexOf("/"));
                    var userID = $("#userID").text().split("|")[0].trim();
                    console.log(userID);
                    $.ajax({
                        url: window.location.origin + baseURL + "/get_student_bid_calendar?userID=" + userID,
                        method: "GET"
                    }).done(function (data) {
                        var events = [];
                        for (var i = 0; i < data.length; i++) {
                            console.log(data[i]);
                            events.push(data[i]);
                            console.log(events);
                        }

                        callback(events);
                    });
                }
            });
        }
        ;

        initCalendar();


    });
</script>