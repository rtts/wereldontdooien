from django.shortcuts import render, redirect
from wereldontdooien.models import Fonkel

def home(request):
#todo try catch
    [current, previous] = Fonkel.objects.order_by("-order").filter(gepubliceerd__isnull=False)[:2]
        
    return render(request, "index.html", {
            "fonkel": current,
            "previous": previous,
            "next": False,
            })

def fonkel(request, nr):
    current = get_object_or_404(Fonkel, order=nr)
    try:
        previous = (Fonkel.objects
                    .order_by("-order")
                    .filter(gepubliceerd__isnull=False)
                    .filter(order__lt=nr)[0]
                    )
    except Fonkel.DoesNotExist:
        previous = False

    try:
        next = (Fonkel.objects
                .order_by("order")
                .filter(gepubliceerd__isnull=False)
                .filter(order__gt=nr)[0]
                )
    except Fonkel.DoesNotExist:
        next = False

    return render(request, "index.html", {
            "fonkel": current,
            "previous": previous,
            "next": next,
            })

def random(request):
    fonkel = Fonkel.objects.order_by("?").filter(gepubliceerd__isnull=False)[0]
    return redirect(fonkel)

def info(request):
    render(request, "info.html", {})
