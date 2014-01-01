from django.conf import settings
from django.conf.urls import patterns, include, url
from django.conf.urls.static import static
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',

    url(r'^$', 'wereldontdooien.views.home', name='home'),
    url(r'^([0-9]+)/$', 'wereldontdooien.views.moment', name='moment'),
    url(r'^verrassing/$', 'wereldontdooien.views.random', name='random'), # ?type=1
    url(r'^achtergrond/$', 'wereldontdooien.views.about', name='about'),

    url(r'^beheer/', include(admin.site.urls)),
) + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
