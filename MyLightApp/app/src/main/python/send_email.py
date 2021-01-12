from email.headerregistry import Address
from email.message import EmailMessage
import os
import smtplib

# Give gmail details of the sender
os.environ['GMAIL_ADDRESS'] = ""
os.environ['GMAIL_APPLICATION_PASSWORD'] = ""
email_address = os.getenv('GMAIL_ADDRESS', None)
email_password = os.getenv('GMAIL_APPLICATION_PASSWORD', None)

# Recipent email details
to_address = (
    Address(display_name='Manvitha', username='kolamanvitha', domain='gmail.com'),
)


def create_email_message(from_address, to_address, subject, body):
    msg = EmailMessage()
    msg['From'] = from_address
    msg['To'] = to_address
    msg['Subject'] = subject
    msg.set_content(body)
    return msg


if __name__ == '__main__':
    msg = create_email_message(
        from_address=email_address,
        to_address=to_address,
        subject='Notification from MyPlantCare',
        body="Hey, Your Spider Plant on MyPlantCare is not receiving enough light and required environment conditions. Move it to a better location for its healthy growth.",
    )

    with smtplib.SMTP('smtp.gmail.com', port=587) as smtp_server:
        smtp_server.ehlo()
        smtp_server.starttls()
        smtp_server.login(email_address, email_password)
        smtp_server.send_message(msg)

    print('Email sent successfully')
