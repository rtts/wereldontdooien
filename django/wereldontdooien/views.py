from django.http import HttpResponse
from django.shortcuts import render, redirect, get_object_or_404
from wereldontdooien.models import PublishedFonkel as Fonkel

def home(request):
    try:
        [current, previous] = (Fonkel.objects
                               .order_by("-id")
                               .filter(zichtbaar=True)[:2]
                               )
    except ValueError:
        return HttpResponse("Oh oh! De wereldontdooisters hebben nog geen fonkels toegevoegd!")
    return render(request, "index.html", {
            "current": current,
            "previous": previous,
            "next": False,
            })

def fonkel(request, nr):
    current = get_object_or_404(Fonkel, id=nr)
    try:
        previous = (Fonkel.objects
                    .order_by("-id")
                    .filter(zichtbaar=True)
                    .filter(id__lt=nr)[0]
                    )
    except Fonkel.DoesNotExist:
        previous = False

    try:
        next = (Fonkel.objects
                .order_by("id")
                .filter(zichtbaar=True)
                .filter(id__gt=nr)[0]
                )
    except Fonkel.DoesNotExist:
        next = False

    return render(request, "index.html", {
            "current": current,
            "previous": previous,
            "next": next,
            })

def random(request):
    try:
        fonkel = (Fonkel.objects
              .order_by("?")
              .filter(zichtbaar=True)[0]
              )
    except IndexError:
        return HttpResponse("Oh oh! De wereldontdooisters hebben nog geen fonkels toegevoegd!")

    return redirect(fonkel)

def info(request):
    render(request, "info.html", {})
