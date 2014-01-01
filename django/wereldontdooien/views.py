from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
def home(request):
    return HttpResponse('<img src="/media/32.jpg"/>')

def moment(request, nr):
    return HttpResponse("Jouw adem ruikt...")

def random(request):
    return HttpResponse("Laat jezelf verrassen!")

def about(request):
    return HttpResponse("Wij zijn de Wereldontdooisters")

