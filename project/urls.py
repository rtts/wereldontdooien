from django.conf import settings
from django.conf.urls import url
from django.conf.urls.static import static
from django.contrib import admin
from wereldontdooien.views import *

urlpatterns = [
    url(r'^$', newhome, name='home'),
#    url(r'^$', home, name='home'),
#    url(r'^([0-9]+)/$', fonkel, name='fonkel'),
#    url(r'^nadja/$', nadja),
#    url(r'^nadja/([0-9]+)/$', nadja, name='nadja'),
#    url(r'^verrassing/$', random, name='random'), # ?type=1
#    url(r'^publish/$', 'wereldontdooien.views.publish', name='publish'),
#    url(r'^api/$', api, name='api'),
    url(r'^beheer/', admin.site.urls),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
