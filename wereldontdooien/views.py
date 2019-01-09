from django.core.exceptions import PermissionDenied
from django.http import HttpResponse, HttpResponseNotAllowed
from django.shortcuts import render, redirect, get_object_or_404
from django.contrib.auth.decorators import login_required
from wereldontdooien.models import PublishedFonkel as Fonkel
from wereldontdooien.models import UnpublishedFonkel
import json

EMPTY_DB_ERROR = "Oh oh! De wereldontdooisters hebben nog geen fonkels toegevoegd!"

def newhome(request):
    current = Fonkel.objects.order_by("?").first()
    return render(request, "index.html", {
        "current": current,
    })

def home(request):
    try:
        current = Fonkel.objects.all()[0]
    except IndexError:
        current = ""

    #return render(request, "splash.html", {
    #        "current": current,
    #        })

    return redirect(current);

@login_required
def nadja(request, nr=''):
    if not nr:
        return redirect(UnpublishedFonkel.objects.all()[0])

    current = get_object_or_404(UnpublishedFonkel, id=nr)
    try:
        previous = UnpublishedFonkel.objects.order_by("-order").filter(order__lt=current.order)[0]
        print ("Before %d comes %d" % (current.id, previous.id))
    except IndexError:
        previous = False
    try:
        next = UnpublishedFonkel.objects.order_by("order").filter(order__gt=current.order)[0]
        print ("After %d comes %d" % (current.id, next.id))
    except IndexError:
        next = False
    return render(request, "index.html", {
            "current": current,
            "previous": previous,
            "next": next,
            })

def fonkel(request, nr):
    current = get_object_or_404(Fonkel, id=nr)
    try:
        previous = Fonkel.objects.filter(id__lt=nr)[0]
    except IndexError:
        previous = False
    try:
        next = Fonkel.objects.order_by("id").filter(id__gt=nr)[0]
    except IndexError:
        next = False
    return render(request, "index.html", {
            "current": current,
            "previous": previous,
            "next": next,
            })

def random(request):
    results = Fonkel.objects.order_by("?")

    if "not" in request.GET:
        results = results.exclude(id=request.GET["not"])

    if "type" in request.GET:
        results = results.exclude(type=request.GET["type"])

    try:
        fonkel = results[0]
    except IndexError:
        return HttpResponse(EMPTY_DB_ERROR)
    return redirect(fonkel)

def publish(request):
    if not request.user.is_authenticated():
        raise PermissionDenied;
    if not request.method == "POST":
        return HttpResponseNotAllowed(["POST"], "<h1>405 Method Not Allowed</h1>");
    if not request.user.has_perm("wereldontdooien.add_publishedfonkel"):
        raise PermissionDenied;

    fonkel = get_object_or_404(UnpublishedFonkel, id=request.POST["fonkel"])
    published_fonkel = fonkel.publish()
    return redirect("/beheer/wereldontdooien/publishedfonkel")

def api(request):
    my_list = list(Fonkel.objects.order_by('id').values('afbeelding','type'))
    return HttpResponse(json.dumps(my_list))
