from django.conf import settings
from django.conf.urls import patterns, include, url
from django.conf.urls.static import static
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',

    url(r'^$', 'wereldontdooien.views.home', name='home'),
    url(r'^([0-9]+)/$', 'wereldontdooien.views.fonkel', name='fonkel'),
    url(r'^nadja/$', 'wereldontdooien.views.nadja'),
    url(r'^nadja/([0-9]+)/$', 'wereldontdooien.views.nadja', name='nadja'),
    url(r'^verrassing/$', 'wereldontdooien.views.random', name='random'), # ?type=1
    url(r'^info/$', 'wereldontdooien.views.info', name='info'),
    url(r'^publish/$', 'wereldontdooien.views.publish', name='publish'),
    url(r'^api/$', 'wereldontdooien.views.api', name='api'),

    url(r'^beheer/', include(admin.site.urls)),
) + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
