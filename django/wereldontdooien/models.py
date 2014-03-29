from uuid import uuid4
from django.db import models
from django.contrib.auth.models import User
from adminsortable.models import Sortable

def imgfile(instance, filename):
    ext = filename.split('.')[-1]
    return '{}.{}'.format(uuid4().hex, ext)

class BaseFonkel(models.Model):
    aangemaakt = models.DateTimeField(auto_now_add=True)
    gewijzigd = models.DateTimeField(auto_now=True)
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
    gebruiker = models.ForeignKey(User)

    def __unicode__(self):
        return self.tekst

    class Meta:
        abstract = True

class UnpublishedFonkel(Sortable, BaseFonkel):
    class Meta(Sortable.Meta):
        verbose_name = "toekomstige fonkel"

    def publish(self):
        published_fonkel = PublishedFonkel(
            gebruiker = self.gebruiker,
            tekst = self.tekst,
            type = self.type,
            afbeelding = self.afbeelding)
        published_fonkel.save()
        self.delete()
        return published_fonkel;

class PublishedFonkel(BaseFonkel):
    def get_absolute_url(self):
        return "/%i/" % self.id

    # This method is only needed once, and will not be part of the GUI
    def unpublish(self):
        unpublished_fonkel = UnpublishedFonkel(
            gebruiker = self.gebruiker,
            tekst = self.tekst,
            type = self.type,
            afbeelding = self.afbeelding)
        fonkels = UnpublishedFonkel.objects.all()
        position = fonkels[0].order - 1
        if position < 0:
            position = 0
            # Please never do this in production code.
            for f in fonkels:
                f.order += 1
                f.save()
        unpublished_fonkel.save()
        unpublished_fonkel.order = position
        unpublished_fonkel.save()
        self.delete()

    class Meta:
        verbose_name = "gepubliceerde fonkel"
        ordering = ["-id"]
