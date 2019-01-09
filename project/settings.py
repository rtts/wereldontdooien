import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

try:
    import uwsgi
    DEBUG = False
except:
    DEBUG = True

ADMINS = (('JJ', 'jj@rtts.eu'),)
SERVER_EMAIL = 'wereldontdooien@rtts.eu'

ALLOWED_HOSTS = ['*']
LOGIN_URL = 'admin:login'
ROOT_URLCONF = 'project.urls'
WSGI_APPLICATION = 'project.wsgi.application'
USE_TZ = True
TIME_ZONE = 'Europe/Amsterdam'
LANGUAGE_CODE = 'nl'
USE_I18N = True
USE_L10N = True
LOCALE_PATHS = (os.path.join(BASE_DIR, "locale"),)
TEMPLATE_DEBUG = DEBUG
STATIC_URL = '/static/'
STATICFILES_DIRS = (os.path.join(BASE_DIR, "files"),)
STATIC_ROOT = '/srv/wereldontdooien/static'
MEDIA_ROOT = '/srv/wereldontdooien/media'
MEDIA_URL = '/media/'
SECRET_KEY = 'pakbta+b_wapi93&chh)o&2qwuufd2(zy^=4b^-&4hz8*isa)9'

TEMPLATE_DIRS = (
    os.path.join(BASE_DIR, "templates"),
)

INSTALLED_APPS = (
    'adminsortable',
    'django_extensions',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'wereldontdooien'
)

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        'NAME': 'wereldontdooien',
        'USER': 'wereldontdooien',
    }
}
