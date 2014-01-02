from uuid import uuid4
from django.db import models
from adminsortable.models import Sortable

def imgfile(instance, filename):
    ext = filename.split('.')[-1]
    return '{}.{}'.format(uuid4().hex, ext)

class Fonkel(Sortable):
    aangemaakt = models.DateTimeField(auto_now_add=True)
    gewijzigd = models.DateTimeField(auto_now=True)
    gepubliceerd = models.DateTimeField(null=True, blank=True)
    zichtbaar = models.BooleanField(default=False)
    type = models.IntegerField(choices=(
            (1, "Spreuk"),
            (2, "Tip"),
            (3, "Persoonlijke opdracht"),
            (4, "Opdracht in gezelschap"),
            (5, "Kadootje"),
            (6, "Complimentje"),
            (7, "Overig"),
            ))
    tekst = models.CharField(max_length=1000)
    afbeelding = models.ImageField(upload_to=imgfile)

    def __unicode__(self):
        return self.tekst

#    class Meta(Sortable.Meta):
#        verbose_name = "dagelijks geluksmomentje"
#        verbose_name_plural = "dagelijkse geluksmomentjes"
